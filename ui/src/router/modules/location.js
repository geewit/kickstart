/** When your routing table is too long, you can split it into small modules**/

import Layout from '@/views/layout/Layout'

const projectRouter = {
  path: '/location',
  component: Layout,
  redirect: 'noredirect',
  name: 'location',
  meta: {
    title: '地区管理',
    icon: 'project'
  },
  children: [
    {
      path: 'country',
      component: () => import('@/views/location/country'),
      name: 'country',
      meta: {
        title: '国家管理',
        icon: 'realestate'
      }
    }
  ]
}

export default projectRouter
