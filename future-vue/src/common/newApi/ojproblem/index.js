import { ajax } from "@/common/api";
export function getRandomProblem(params) {
  return ajax("/api/get-random-problem", "get", {
    params,
  });
}
export function getUserGroupProblemStatus(data) {
  return ajax("/api/get-user-group-problem-status", "post", {
    data,
  });
}
export function getIoRecord(params) {
  return ajax("/api/admin/problem/get-io-record", "get", {
    params,
  });
}
export function batchUploadProblems(data) {
  return ajax("/api/admin/problem/batch-zip", "post", {
    data,
  });
}