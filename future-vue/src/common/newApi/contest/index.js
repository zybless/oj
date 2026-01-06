import { ajax } from "@/common/api";
export function startContest(cid) {
  return ajax("/api/start-contest?cid=" + cid, "post", {});
}
export function getStartTime(params) {
  return ajax("/api/get-contest-start-time", "get", {
    params,
  });
}
