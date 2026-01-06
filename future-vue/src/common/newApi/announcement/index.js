import { ajax } from "@/common/api";
export function getAnnouncementList(id) {
  let params = {
    id,
  };
  return ajax("/api/getAnnouncementById", "get", {
    params,
  });
}
