import { ajax } from "@/common/api";
export function startExercise(params) {
  return ajax("/api/exam-judge/start_exercise", "get", {
    params,
  });
}
export function submitExercise(data) {
  return ajax("/api/exam-judge/submit_exercise", "post", {
    data,
  });
}
