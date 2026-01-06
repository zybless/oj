import { ajax } from "@/common/api";
export function getTrainCampTeam() {
  return ajax("/api/admin/training-camp-team", "get");
}
export function updateTrainCampTeam(data) {
  return ajax("/api/admin/training-camp-team", "put", {
    data,
  });
}
export function getTrainCamp(id) {
  return ajax("/api/admin/training-camp-team/training-camp", "get", {
    params: { id },
  });
}
export function addTrainCamp(data) {
  return ajax("/api/admin/training-camp-team/training-camp", "post", {
    data,
  });
}
export function updateTrainCamp(data) {
  return ajax("/api/admin/training-camp-team/training-camp", "put", {
    data,
  });
}
export function deleteTrainCamp(id) {
  return ajax("/api/admin/training-camp-team/training-camp", "delete", {
    params: { id },
  });
}
