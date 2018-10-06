import request from '@/utils/request'

const api_prefix = 'api/business/study_abroad'

export function fetchPage(query) {
  return request.http({
    url: api_prefix + '/page',
    method: 'get',
    params: query
  })
}

export function fetchList(query) {
  return request.http({
    url: api_prefix + '/list',
    method: 'get',
    params: query
  })
}

export function fetch(id) {
  return request.http({
    url: api_prefix + '/' + id,
    method: 'get',
    params: { id }
  })
}

export function create(data) {
  return request.http({
    url: api_prefix,
    method: 'post',
    data
  })
}

export function update(data) {
  return request.http({
    url: api_prefix + '/' + data.id,
    method: 'put',
    data
  })
}
