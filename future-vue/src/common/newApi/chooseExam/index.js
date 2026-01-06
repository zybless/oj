import { ajax } from "@/common/api";
export function getChooseExamList(params) {
  return ajax("/api/admin/exam/get-exam-list", "get", {
    params,
  });
}
export function getExamList(params) {
  return ajax("/api/exam/get-exam-list", "get", {
    params,
  });
}
export function createChooseExam(data) {
  return ajax("/api/admin/exam", "post", {
    data,
  });
}
export function getChooseExamHistory(id) {
  return ajax("/api/admin/exam/get-exam-history", "get", {
    params: { id },
  });
}
export function updateChooseExam(data) {
  return ajax("/api/admin/exam", "put", {
    data,
  });
}
export function deleteChooseExam(id) {
  return ajax("/api/admin/exam", "delete", {
    params: { id },
  });
}
export function getChooseExamById(id) {
  return ajax("/api/admin/exam", "get", {
    params: { id },
  });
}
export function getExam(examId) {
  return ajax("/api/exam", "get", {
    params: { examId },
  });
}
export function updateChooseProblems(data, repoId) {
  return ajax(`/api/admin/exam/update-problems?repoId=${repoId}`, "put", {
    data,
  });
}
