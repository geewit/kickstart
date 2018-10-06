import moment from 'moment'

/**
 * 通用filter
 */
// 空值过滤器
export function nullValFilter(value) {
  return (value === null || value === undefined) ? 'null' : value
}
// 为空
export function isEmpty(value) {
  if (value === null || value === undefined) {
    return true
  }
  if (value.length === 0) {
    return true
  }
  return value instanceof String && value.trim().length === 0
}
// 非空
export function isNotEmpty(value) {
  return !isEmpty(value)
}
// 空对象
export function isObjectEmpty(obj) {
  if (obj === null || obj === undefined) {
    return true
  }
  if (obj.constructor !== Object) {
    return obj.length === 0
  }
  return Object.keys(obj).length === 0
}
// 是否为数字
export function isRealNum(val) {
  // isNaN()函数 把空串 空格 以及NUll 按照0来处理 所以先去除
  if (val === '' || val == null) {
    return false
  }
  return !isNaN(val)
}

// 日期-时间 格式化 YYYY
export function year(value) {
  if (value) {
    return moment(value).format('YYYY')
  }
  return ''
}
// 日期-时间 格式化 YYYY-MM
export function yearmonth(value) {
  if (value) {
    return moment(value).format('YYYY-MM')
  }
  return ''
}
// 日期格式化 YYYY-MM-DD
export function date(value) {
  if (value) {
    return moment(value).format('YYYY-MM-DD')
  }
  return ''
}
// 日期-时间 格式化 YYYY-MM-DD HH:mm:ss
export function datetime(value) {
  if (value) {
    return moment(value).format('YYYY-MM-DD HH:mm:ss')
  }
  return ''
}

/* 公共方法 */
// 计算表格高度
export function calcTableHeight() {
  // console.log("calcTableHeight");
  // 窗口 高度
  const clientHeight = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight
  // console.log("clientHeight:" + clientHeight);

  // toolbar 高度
  let toolBarHeight = 0
  const toolbarNum = document.getElementsByClassName('toolbar').length
  if (toolbarNum && toolbarNum > 0) {
    for (let i = 0; i < toolbarNum; i++) {
      toolBarHeight += document.getElementsByClassName('toolbar')[i].offsetHeight
    }
  }
  // console.log("toolBarHeight:" + toolBarHeight);

  // 可用于显示表格的高度
  // 表格可用高度 = 浏览器窗口可视高度 - 顶部levelbar高度40 - 顶部标签栏高度40 - 头部toolbar高度  - 底部页码栏40 - 空余高度20
  const tableHeight = clientHeight - 40 - 40 - toolBarHeight - 40 - 30

  /* // 表格每一行高度,默认40
  let tableCellHeight = 40;
  if (document.getElementsByClassName("el-table__row")[0]){
      tableCellHeight = document.getElementsByClassName("el-table__row")[0].offsetHeight;
  }
  // console.log("tableCellHeight:" + tableCellHeight);

  // 处理表格高度为单元格高度整数倍
  if (tableHeight % tableCellHeight !== 0) {
      tableHeight = tableHeight - tableHeight % tableCellHeight;
  }
  console.log("tableHeight:" + tableHeight);*/

  return tableHeight
}
