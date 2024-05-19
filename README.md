Best Route Problem

Technologies Used
1. Java 8
2. Maven 3.6.9
3. Springboot
4. JPA
5. Mysql
6. REST
To compile project use mvn clean install -U

——————————————————————————————————————————————————————————————————————————————
Extra Features
1. Used Design Patterns(i.e strategy pattern to calculate distance so that if new algorithms comes in future we can implement it without changing to much code)
2. Add Custom Exceptions
3. Aspect Oriented Programming
    1. @EnableLogging-> when use this on method level it will print complete request and response of that method
    2. @ValidateRequestAccess -> If This is added on any of the method then we need to pass auth headers otherwise it will throw unauthorised error
4. Generic JPA Implementation so that for any kind of id generator(Long/String) we can use predefined methods 
5. Added Phone Number Validator so that we can add phone numbers in +91XXXXXXXXXX format only
6. Did not expose DB entity to any of the response directly, transform entity to entry before sending response to client
7. Flow of api will be like this
    1. Controller->Manager->Dao->Entity
8. If minPreparation time provided in place order request then we will pick this else we are generating random number b/w 1-30 mins


——————————————————————————————————————————————————————————————————————————————
Functionalities

User
    1. Add User -> Uniqueness on Phone Number
    2. Update User
    3. Upsert Address For User

Restaurant
    1. createRestaurant (Restaurant+address)
    2. updateRestaurant
    3. Set restaurant as serviceable true/false

DeliveryAgent
    1. Create Agent (with basic details + current location’s latitude and longitude)
    2. Update Agent (details + current location’s latitude and longitude)

OrderFulfilment
    1. PlaceOrder
    2. AssignOrder
    3. Find Min Cost Path To Deliver Orders

——————————————————————————————————————————————————————————————————————————————

Basic Checks 
1. User can place order from serviceable restaurant only
2. System should assign orders to serviceable/active deliveryAgents only on basis of location and currently offline or not
3. If agent has 2 active(Assigned+Picked) orders then we do not allow more order assignment to that user
4. Uniqueness on Phone number on user table, same can be implemented on Restaurant and DeliveryAgent Table as well
5. Since User/Restaurant can change their details after order confirmation but before delivery so to tackle this we are storing snapshot of restaurant and user inside order table and using that info for further cost calculation algo

 Assumptions
1. System is responsible to find delivery agent who is available for order assignment
2. Assignment should be on same time  for 2 users from 2 different restaurants to same delivery agent
3. Restaurant will start preparing order at the same time after order assignment
4. Speed of Deliver agent is 20KMPH
5. To solve Problem We Are Assuming that Both Restaurants and users have different name(since we are using name as key in map while solving problem -> we can move this to Id basis key in future scope)
6. Here we strictly consider that Agent won’t go to a customer without picking his/her order(Means agent won’t go to Customer2 Before Restaurant2, same won’t go to Customer1 Before Restaurant1)


Future Scope
1. UI client send delivery agents’ live locations so that we can update current locations of delivery agents via Cassandra to Agent Database
2. We can introduce Virtual number for a delivery transaction to ensure privacy of users





Problem Solving Approach
1. If agent has only one order
    1. In this case there will be only one path Agent-> Restaurant -> Customer
    2. In this case Cost will be (Max(AgentLocation->RestaurantLocation, orderPreparationTime)+(RestaurantLocation-> CustomerLocation)
2. If agent gets 2 orders at the same time(main problem)


There are 5 actors while finding minimum cost path
1. Aman(Agent)
2. Restaurant1
3. Customer1
4. Restaurant2
5. Customer2

Creating map for each actors with the current location of each actor with preparation time

Map<String, Location> locations = new HashMap<>();
locations.put("Aman", new Location("Aman", lat_Aman, lon_Aman, 0));
locations.put("R1", new Location("R1", lat_R1, lon_R1, preparationTime1));
locations.put("R2", new Location("R2", lat_R2, lon_R2, preparationTime2));
locations.put("C1", new Location("C1", lat_C1, lon_C1, 0));
locations.put("C2", new Location("C2", lat_C2, lon_C2, 0));

Now adding possible edge starting from actor in map which tell us that from a particular position where agent can go next

        // Define the edges representing travel times
        Map<String, List<Edge>> graph = new HashMap<>();
graph.put("Aman", Arrays.asList(new Edge("R1", haversineDistance(locations.get("Aman"), locations.get("R1"))),
        new Edge("R2", haversineDistance(locations.get("Aman"), locations.get("R2")))));
graph.put("R1", Arrays.asList(new Edge("C1", haversineDistance(locations.get("R1"), locations.get("C1"))),
        new Edge("R2", haversineDistance(locations.get("R1"), locations.get("R2"))),
        new Edge("C2", haversineDistance(locations.get("R1"), locations.get("C2")))));
graph.put("R2", Arrays.asList(new Edge("C1", haversineDistance(locations.get("R2"), locations.get("C1"))),
        new Edge("R1", haversineDistance(locations.get("R2"), locations.get("R1"))),
        new Edge("C2", haversineDistance(locations.get("R2"), locations.get("C2")))));
graph.put("C1", Arrays.asList(new Edge("R2", haversineDistance(locations.get("C1"), locations.get("R2"))),
        new Edge("C2", haversineDistance(locations.get("C1"), locations.get("C2")))));
graph.put("C2", Arrays.asList(new Edge("R1", haversineDistance(locations.get("C2"), locations.get("R1"))),
        new Edge("C1", haversineDistance(locations.get("C2"), locations.get("C1")))));




Below are the 6 possible ways to deliver 2 orders for 2 customers from 2 restaurants 
Here we strictly consider that Agent won’t go to a customer without picking his/her order(Means agent won’t go to Customer2 Before Restaurant2, same won’t go to Customer1 Before Restaurant1)

Aman->R1->C1->R2->C2
Aman->R2->C2->R1->C1
Aman->R1->R2->C1->C2
Aman->R1->R2->C2->C1
Aman->R2->R1->C1->C2
Aman->R2->R2->C2->C1

Now we will use modified dijkstra algorithm to calculate the time from a location to another location

When agent reaches any of the restaurant then time taken will be maximum of (preparationTime, TimeTaken to travel that restaurant from order assignment), i.e. Agent reaches Restaurant1 in 10 minutes but preparation time is 20 minutes then Agent has to wait for 10 mins to pick the order so cost for this operation will be 20 not 10.
To Clarify this let’s keep another example

lets suppose Agent takes this path

Agent->Restaurant1->Customer1->Restaurant2->Customer2
Let’s say Restaurant1 preparation time is 20 min and Restaurant2 preparation time is 50minutes
Agent->Restaurant1 travel time =10 mins
Restaurant1->customer1 travel time is 5 mins
Customer1->Restaurant2 travel time is 20 mins
Restaurant2->Customer2 travel time is 10 mins



Total time in mins = 0;
Agent->Restaurant1 path cost = max(totalTime, foodPreparation) = (10,20)=>20mins
Total time in mins = 20

Restaurant1->Customer1 -> 5 mins
totalTime =20+5=25 mins

Customer1->Restaurant2  cost->20 mins
totalTime = 25+20 = 45 mins  now since food preparation time is 5 mins so agent has to wait 5 more minutes
totalTime in mins = max(45,50)=>50 min

Restaurant2 ->Customer 2 cost 10 min
totalCost = 50+10 -> 60 mins 

Thus total cost of this operation will be 60 mins




