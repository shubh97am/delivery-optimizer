package com.lucidity.deliveryoptimizer.common.constants;

public class Constant {
    private Constant() {

    }

    public static class Common {
        private Common() {

        }

        public static final Long ACTIVE_ORDER_THRESHHOLD_VALUE_FOR_AGENT = 2L;
    }


    public static class Controller {
        private Controller() {

        }

        public static final String BASE_URL = "/api/deliverycost-optimization-service";
    }

    public static class HeartBeatController {
        private HeartBeatController() {
        }

        public static final String HEARTBEAT_BASE_PATH = "/heartbeat";

    }

    public static class UserController {
        private UserController() {
        }

        public static final String CREATE_USER = "/users";
        public static final String GET_USER = "/users/{userId}";
        public static final String UPDATE_USER = "/users/{userId}";
        public static final String ADD_UPDATE_ADDRESS_FOR_USER = "/users/{userId}/address";
        public static final String GET_ALL_USERS = "/users/all";

    }


    public static class RestaurantController {
        private RestaurantController() {
        }

        public static final String CREATE_RESTAURANT = "/restaurants";
        public static final String GET_RESTAURANT = "/restaurants/{restaurantId}";
        public static final String UPDATE_RESTAURANT = "/restaurants/{restaurantId}";
        public static final String UPDATE_RESTAURANT_SERVICEABILITY = "/restaurants/{restaurantId}/serviceability";
        public static final String GET_ALL_RESTAURANT = "/restaurants/all";

    }

    public static class DeliveryAgentController {
        private DeliveryAgentController() {
        }

        public static final String CREATE_AGENT = "/agents";
        public static final String GET_AGENT = "/agents/{agentId}";
        public static final String UPDATE_AGENT_BASIC_DATA = "/agents/{agentId}/basic";
        public static final String UPDATE_AGENT_SERVICEABILITY = "/agents/{agentId}/serviceability";
        public static final String UPDATE_AGENT_CURRENT_LOCATION = "/agents/{agentId}/location";
        public static final String GET_ALL_AGENTS = "/agents/all";

    }

    public static class OrderFulfillmentController {
        private OrderFulfillmentController() {
        }

        public static final String PLACE_ORDER = "/orders";
        public static final String ASSIGN_ORDER = "/orders/assignOrder";
        public static final String GET_ACTIVE_ORDERS_FOR_AGENT = "/orders/agent/{agentId}/fetch/active";
        public static final String EXECUTE_DELIVERY_TASK = "/orders/execute/delivery";

    }

    public static class Logging {
        private Logging() {

        }

        public static final String KEY_CLASS_NAME = "className";
        public static final String KEY_METHOD_NAME = "methodName";
        public static final String KEY_METHOD_ARGUMENTS = "methodArguments";
        public static final String KEY_METHOD_PHASE = "methodPhase";
        public static final String KEY_RETURN_VALUE = "returnValue";
        public static final String KEY_TIME_ELAPSED_IN_MILLIS = "timeElapsedInMillis";
        public static final String KEY_CAUSE = "cause";
        public static final String KEY_STACK_TRACE = "stackTrace";
        public static final String KEY_LOCALIZED_MESSAGE = "localizedMessage";
        public static final String KEY_SUPPRESSED = "suppressed";
        public static final String KEY_MESSAGE = "message";
        public static final String KEY_TIME = "time";
        public static final String KEY_EPOCH_TIME = "epochTime";
        public static final String METHOD_PHASE_STARTED = "started";
        public static final String METHOD_PHASE_COMPLETED = "completed";
        public static final String METHOD_PHASE_EXCEPTION_THROWN = "exceptionThrown";
    }
}
