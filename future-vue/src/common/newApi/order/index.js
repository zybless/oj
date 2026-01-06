import { ajax } from "@/common/api";
export function getOrdersList(params) {
  return ajax("/api/admin/orders/get-orders-list", "get", {
    params,
  });
}