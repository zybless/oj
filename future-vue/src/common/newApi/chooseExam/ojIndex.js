import { ajax } from "@/common/api";
export function startExam(examId, password) {
  return ajax("/api/exam-judge/start_exam", "get", {
    params: { examId, password },
  });
}
export function submitExam(data) {
  return ajax("/api/exam-judge/submit_exam", "post", {
    data,
  });
}
export function getExamRank(params) {
  return ajax("/api/admin/exam/get-exam-rank", "get", {
    params,
  });
}
export function getHistory(params) {
  return ajax("/api/admin/exam/get-exam-history-detail", "get", {
    params,
  });
}
