import request from '../utils/request'

export const getOnlineProjects = (params: any) => {
  return request.get('/public/project/list', { params })
}

export const getProjectDetail = (id: number) => {
  return request.get(`/public/project/${id}`)
}

export const createProject = (data: any) => {
  return request.post('/sponsor/project/create', data)
}

export const updateProject = (data: any) => {
  return request.put('/sponsor/project/update', data)
}

export const cancelProject = (id: number) => {
  return request.put(`/sponsor/project/cancel/${id}`)
}

export const getMyProjects = (params: any) => {
  return request.get('/sponsor/project/my-list', { params })
}

export const getProjectSupporters = (id: number, params: any) => {
  return request.get(`/sponsor/project/${id}/supporters`, { params })
}
