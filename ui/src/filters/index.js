// set function parseTime,formatTime to filter
import request from '@/utils/request'
import { isEmpty } from '@/utils/filter'

export * from '@/utils'

function pluralize(time, label) {
  if (time === 1) {
    return time + label
  }
  return time + label + 's'
}

export function timeAgo(time) {
  const between = Date.now() / 1000 - Number(time)
  if (between < 3600) {
    return pluralize(~~(between / 60), ' minute')
  } else if (between < 86400) {
    return pluralize(~~(between / 3600), ' hour')
  } else {
    return pluralize(~~(between / 86400), ' day')
  }
}

/* 数字 格式化*/
export function numberFormatter(num, digits) {
  const si = [
    { value: 1E18, symbol: 'E' },
    { value: 1E15, symbol: 'P' },
    { value: 1E12, symbol: 'T' },
    { value: 1E9, symbol: 'G' },
    { value: 1E6, symbol: 'M' },
    { value: 1E3, symbol: 'k' }
  ]
  for (let i = 0; i < si.length; i++) {
    if (num >= si[i].value) {
      return (num / si[i].value + 0.1).toFixed(digits).replace(/\.0+$|(\.[0-9]*[1-9])0+$/, '$1') + si[i].symbol
    }
  }
  return num.toString()
}

export function toThousandslsFilter(num) {
  return (+num || 0).toString().replace(/^-?\d+/g, m => m.replace(/(?=(?!\b)(\d{3})+$)/g, ','))
}

export function gwPropertyFilter(value, parentId) {
  if (isEmpty(parentId)) {
    return null
  }

  const url = 'api/properties/' + parentId + '/children'
  const listJson = window.sessionStorage.getItem(url)
  let obj = listJson ? JSON.parse(listJson) : null
  if (obj == null) {
    try {
      const reponse = request.sync_get(url)
      console.log(reponse)
      if (reponse && reponse.length > 0) {
        window.sessionStorage.setItem(url, reponse)
      }
      // 第一次进来也要执行转化
      obj = JSON.parse(reponse)
    } catch (e) {
      return null
    }
  }
  // 匹配
  let isMatch = false
  if (obj != null) {
    for (const key in obj) {
      console.log(key)
      if (value === obj[key]) {
        isMatch = true
        return key
      }
    }
    if (!isMatch) {
      return parentId
    }
  }
  return null
}
