import http from './http'

export function fetchReviewList(status) {
  return http.get('/api/leave/reviewer/applications', {
    params: {
      status,
    },
  })
}

export function fetchReviewDetail(id) {
  return http.get(`/api/leave/reviewer/applications/${id}`)
}

export function approveLeave(id, comment) {
  return http.post(`/api/leave/reviewer/applications/${id}/approve`, { comment })
}

export function rejectLeave(id, comment) {
  return http.post(`/api/leave/reviewer/applications/${id}/reject`, { comment })
}
