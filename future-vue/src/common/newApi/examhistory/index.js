import { ajax } from "@/common/api";
export function getExerciseHistory(repoId) {
  return ajax("/api/exam-history/exercise", "get", {
    params: { repoId },
  });
}

export function getExerciseHistoryDetail(params) {
  return ajax("/api/exam-history/exercise-detail", "get", {
    params,
  });
}
