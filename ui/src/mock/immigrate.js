import Mock from 'mockjs'
import { param2Obj } from '@/utils'

const List = []
const count = 100

const image_uri = 'https://wpimg.wallstcn.com/e4558086-631c-425c-9430-56ffb46e70b3'

for (let i = 0; i < count; i++) {
  List.push(Mock.mock({
    id: '@increment',
    name: Mock.mock('@county(true)'),
    locationId: 'L00001',
    'estateTypes|2': ['独栋别墅', '联排别墅', '公寓', '商铺'],
    commission: '@float(0, 100, 2, 2)',
    recommended: Mock.Random.boolean(),
    'status|1': ['published', 'draft', 'deleted'],
    createTime: '@datetime',
    image_uri
  }))
}

export default {
  getPage: config => {
    const { importance, type, title, page = 1, limit = 20, sort } = param2Obj(config.url)

    let mockList = List.filter(item => {
      if (importance && item.importance !== +importance) return false
      if (type && item.type !== type) return false
      return !(title && item.title.indexOf(title) < 0)
    })

    if (sort === '-id') {
      mockList = mockList.reverse()
    }

    const pageList = mockList.filter((item, index) => index < limit * page && index >= limit * (page - 1))

    return {
      total: mockList.length,
      items: pageList
    }
  },
  get: (config) => {
    const { id } = param2Obj(config.url)
    for (const estate of List) {
      if (estate.id === +id) {
        return estate
      }
    }
  },
  create: () => ({
    data: 'success'
  }),
  update: () => ({
    data: 'success'
  })
}
