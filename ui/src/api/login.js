import request from '@/utils/request'

export function loginByUsername(username, password) {
  const data = {
    username,
    password
  }
  return request.http({
    url: '/api/login',
    method: 'post',
    data
  })
}

export function logout() {
  return request.http({
    url: '/api/logout',
    method: 'post'
  })
}

export function getUserInfo(token) {
  return request.http({
    url: '/api/user/info',
    method: 'get',
    params: { token }
  })
}

