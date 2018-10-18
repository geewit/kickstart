/** When your routing table is too long, you can split it into small modules**/

import Layout from '@/views/layout/Layout'

const locationRouter = {
  path: '/location',
  component: Layout,
  name: 'location',
  redirect: 'noredirect',
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

export default locationRouter
