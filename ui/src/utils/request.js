import axios from 'axios'
import { Message } from 'element-ui'
import store from '@/store'
import { getToken, TokenKey } from '@/utils/auth'

// create an axios instance
const service = axios.create({
  // baseURL: process.env.BASE_API, // api的base_url
  timeout: 5000, // request timeout
  withCredentials: true,
  headers: {
    post: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  }
})

// request interceptor
service.interceptors.request.use(
  config => {
    // Do something before request is sent
    if (store.getters.token) {
      // 让每个请求携带token-- ['X-Token']为自定义key 请根据实际情况自行修改
      config.headers[TokenKey] = getToken()
    }
    return config
  },
  error => {
    // Do something with request error
    console.debug('error: ' + JSON.stringify(error)) // for debug
    Message({
      message: (error && error.response && error.response.data) ? error.response.data : error.message,
      type: 'error',
      duration: 5 * 1000
    })
    Promise.reject(error)
  }
)

// respone interceptor
service.interceptors.response.use(
  response => response,
  /**
   * 下面的注释为通过在response里，自定义code来标示请求状态
   * 当code返回如下情况则说明权限有问题，登出并返回到登录页
   * 如想通过xmlhttprequest来状态码标识 逻辑可写在下面error中
   * 以下代码均为样例，请结合自生需求加以修改，若不需要，则可删除
   */
  // response => {
  //   const res = response.data
  //   if (res.code !== 20000) {
  //     Message({
  //       message: res.message,
  //       type: 'error',
  //       duration: 5 * 1000
  //     })
  //     // 50008:非法的token; 50012:其他客户端登录了;  50014:Token 过期了;
  //     if (res.code === 50008 || res.code === 50012 || res.code === 50014) {
  //       // 请自行在引入 MessageBox
  //       // import { Message, MessageBox } from 'element-ui'
  //       MessageBox.confirm('你已被登出，可以取消继续留在该页面，或者重新登录', '确定登出', {
  //         confirmButtonText: '重新登录',
  //         cancelButtonText: '取消',
  //         type: 'warning'
  //       }).then(() => {
  //         store.dispatch('FedLogOut').then(() => {
  //           location.reload() // 为了重新实例化vue-router对象 避免bug
  //         })
  //       })
  //     }
  //     return Promise.reject('error')
  //   } else {
  //     return response.data
  //   }
  // },
  error => {
    console.debug('error: ' + JSON.stringify(error)) // for debug
    Message({
      message: (error && error.response && error.response.data) ? error.response.data : error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export function axios_get(url, params) {
  return new Promise((resolve, reject) => {
    service.get(url, params)
      .then(response => {
        resolve(response.data)
      }, err => {
        reject(err)
      })
      .catch((error) => {
        reject(error)
      })
  })
}

export function axios_post(url, params) {
  return new Promise((resolve, reject) => {
    axios.post(url, params)
      .then(response => {
        resolve(response.data)
      }, err => {
        reject(err)
      })
      .catch((error) => {
        reject(error)
      })
  })
}

export function axios_put(url, params) {
  return new Promise((resolve, reject) => {
    axios.put(url, params)
      .then(response => {
        resolve(response.data)
      }, err => {
        reject(err)
      })
      .catch((error) => {
        reject(error)
      })
  })
}

export function axios_delete(url) {
  return new Promise((resolve, reject) => {
    service.delete(url)
      .then(response => {
        resolve(response.data)
      }, err => {
        reject(err)
      })
      .catch((error) => {
        reject(error)
      })
  })
}

export default {
  http: service,

  get: function(url, params) {
    return axios_get(url, params)
  },

  post: function(url, params) {
    return axios_post(url, params)
  },

  put: function(url, params) {
    return axios_put(url, params)
  },

  delete: function(url) {
    return axios_delete(url)
  },

  axios: function() {
    return axios
  },

  postOrPut: function(url, method, params) {
    if (method.toLowerCase() === 'post') {
      return axios_post(url, params)
    } else {
      return axios_put(url, params)
    }
  }
}
