import { ajax } from "@/common/api";
export function getTrainCampTeam() {
  return ajax("/api/training-camp-team", "get");
}
export function getTrainingCamp(id) {
  return ajax("/api/training-camp-team/training-camp", "get", {
    params: { id },
  });
}
