import { ajax } from "@/common/api";
export function getMonthRanking() {
  return ajax("/api/get-month-ranking", "get");
}
export function updateMonthRanking(data) {
  return ajax("/api/admin/dashboard/update-month-ranking", "post", {
    data,
  });
}
export function getRankingList() {
  return ajax("/api/get-ranking-list", "get");
}
export function updateRankingList() {
  return ajax("/api/admin/dashboard/update-ranking-list", "post");
}