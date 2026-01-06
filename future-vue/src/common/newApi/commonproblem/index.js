import { ajax } from "@/common/api";
export function getCommonProblemList(params) {
  return ajax("/api/admin/exam-problem/get-problem-list", "get", {
    params,
  });
}
export function createCommonProblem(data) {
  return ajax("/api/admin/exam-problem", "post", {
    data,
  });
}
export function getCommonProblemById(id) {
  return ajax("/api/admin/exam-problem", "get", {
    params: { id },
  });
}
export function updateCommonProblem(data) {
  return ajax("/api/admin/exam-problem", "put", {
    data,
  });
}
export function deleteCommonProblem(id) {
  return ajax("/api/admin/exam-problem", "delete", {
    params: { id },
  });
}
export function getProblemListInRepo(params) {
  return ajax("/api/admin/exam-problem/get-problem-list-in-repo", "get", {
    params,
  });
}
export function getProblemListNotInRepo(params) {
  return ajax("/api/admin/exam-problem/get-problem-list-not-in-repo", "get", {
    params,
  });
}