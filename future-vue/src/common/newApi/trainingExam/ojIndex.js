import { ajax } from "@/common/api";
export function getTrainingExamList(params) {
  return ajax("/api/get-training-exam-list", "get", {
    params,
  });
}
