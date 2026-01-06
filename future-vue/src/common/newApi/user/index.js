import { ajax } from "@/common/api";
export function addVip(uuid, date) {
  return ajax(`/api/admin/user/add-vip?uuid=${uuid}&date=${date}`, "post");
}
export function removeVip(uuid) {
  return ajax("/api/admin/user/remove-vip", "delete", {
    params: { uuid },
  });
}
export function getLiveStream() {
  return ajax("/api/livestream", "get");
}
export function updateLiveStream(url) {
  return ajax(`/api/admin/livestream?url=${url}`, "put");
}