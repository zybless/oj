import { ajax } from "@/common/api";
export function getQuestionBankList(params) {
  return ajax("/api/exam-repo/get-repo-list", "get", {
    params,
  });
}
export function getProblemListInRepo(params) {
  return ajax("/api/exam-problem/get-problem-list-in-repo", "get", {
    params,
  });
}
export function getProblemListInRepoExam(params) {
  return ajax("/api/exam-problem/get-problem-list-in-repo-exam", "get", {
    params,
  });
}
export function getProblemListInRepo2(params) {
  return ajax("/api/exam-problem/get-problem-list-in-repo-exam-na", "get", {
    params,
  });
}
export function getRepoById(params) {
  return ajax("/api/exam-repo", "get", {
    params,
  });
}