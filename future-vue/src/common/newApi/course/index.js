import { ajax } from "@/common/api";
export function getCourseList(params) {
  return ajax("/api/admin/course/get-course-list", "get", {
    params,
  });
}
export function createCourse(data) {
  return ajax("/api/admin/course", "post", {
    data,
  });
}
export function updateCourse(data) {
  return ajax("/api/admin/course", "put", {
    data,
  });
}
export function deleteCourse(id) {
  return ajax("/api/admin/course", "delete", {
    params: { id },
  });
}
export function getCourseListUser(params) {
  return ajax("/api/course/get-course-list", "get", {
    params,
  });
}
export function getCourseDetailUser(params) {
  return ajax("/api/course", "get", {
    params,
  });
}
export function uploadImg(data) {
  return ajax("/api/file/upload-img", "post", {
    data,
  });
}
export function deleteImg(url) {
  return ajax("/api/file/delete-img", "post", {
    params: { url },
  });
}
