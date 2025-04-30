package com.example.pharmiczy.Apis.responses;

import java.util.List;

public class OrderResponse {
    private String message;
    private Order order;

    public String getMessage() {
        return message;
    }

    public Order getOrder() {
        return order;
    }

    public static class Order {
        private String _id;
        private String userId;
        private List<Item> items;
        private int totalAmount;
        private String status;
        private String paymentStatus;
        private String address;
        private String contact;
        private String orderedAt;

        public String get_id() {
            return _id;
        }

        public String getUserId() {
            return userId;
        }

        public List<Item> getItems() {
            return items;
        }

        public int getTotalAmount() {
            return totalAmount;
        }

        public String getStatus() {
            return status;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public String getAddress() {
            return address;
        }

        public String getContact() {
            return contact;
        }

        public String getOrderedAt() {
            return orderedAt;
        }

        public static class Item {
            private Medicine medicineId;
            private int quantity;
            private int price;
            private String _id;

            public Medicine getMedicineId() {
                return medicineId;
            }

            public int getQuantity() {
                return quantity;
            }

            public int getPrice() {
                return price;
            }

            public String get_id() {
                return _id;
            }

            public static class Medicine {
                private String _id;
                private String productName;
                private Pricing pricing;

                public String get_id() {
                    return _id;
                }

                public String getProductName() {
                    return productName;
                }

                public Pricing getPricing() {
                    return pricing;
                }

                public static class Pricing {
                    private int sellingPrice;

                    public int getSellingPrice() {
                        return sellingPrice;
                    }
                }
            }
        }
    }
}
