import { ajax } from "@/common/api";
export function getQuestionBankList(params) {
  return ajax("/api/admin/exam-repo/get-repo-list", "get", {
    params,
  });
}
export function createQuestionBank(data) {
  return ajax("/api/admin/exam-repo", "post", {
    data,
  });
}
export function getQuestionBankById(id) {
  return ajax("/api/admin/exam-repo", "get", {
    params: { id },
  });
}
export function updateQuestionBank(data) {
  return ajax("/api/admin/exam-repo", "put", {
    data,
  });
}
export function deleteQuestionBank(id) {
  return ajax("/api/admin/exam-repo", "delete", {
    params: { id },
  });
}
export function updateRepoProblems(data, repoId) {
  return ajax(`/api/admin/exam-repo/update-problems?repoId=${repoId}`, "put", {
    data,
  });
}
