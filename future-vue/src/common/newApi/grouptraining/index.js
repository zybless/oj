import { ajax } from "@/common/api";
import qs from "qs";
export function addTrainingFromPublic(params) {
  return ajax("/api/group/add-training-from-public", "post", {
    params,
    paramsSerializer: (params) =>
      qs.stringify(params, { arrayFormat: "repeat" }),
  });
}
