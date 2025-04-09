package com.example.pharmiczy.Apis.responses;

import java.util.List;

public class OrderResponse {
    private String message;
    private Order order;

    public String getMessage() { return message; }
    public Order getOrder() { return order; }

    public static class Order {
        private String userId;
        private List<Item> items;
        private int totalAmount;
        private String status;
        private String paymentStatus;
        private String address;
        private String contact;
        private String _id;
        private String orderedAt;

        public static class Item {
            private String medicineId;
            private int quantity;
            private int price;
            private String _id;

            // Getters
        }

        // Getters
    }
}
