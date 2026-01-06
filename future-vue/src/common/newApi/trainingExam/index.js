import { ajax } from "@/common/api";
export function getExamList(params) {
  return ajax("/api/admin/training/get-exam-list", "get", {
    params,
  });
}
export function updateExam(data) {
  return ajax("/api/admin/training/exam", "put", {
    data,
  });
}
export function deleteExam(params) {
  return ajax("/api/admin/training/exam", "delete", {
    params,
  });
}
export function addExamFromPublic(data) {
  return ajax("/api/admin/training/add-exam-from-public", "post", {
    data,
  });
}
