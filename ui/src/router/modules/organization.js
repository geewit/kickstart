/** When your routing table is too long, you can split it into small modules**/

import Layout from '@/views/layout/Layout'

const organizationRouter = {
  path: '/table',
  component: Layout,
  redirect: '/table/complex-table',
  name: 'table',
  meta: {
    title: '组织管理',
    icon: 'organization'
  },
  children: [
    /* {
      path: 'dynamic-table',
      component: () => import('@/views/table/dynamicTable/index'),
      name: 'dynamicTable',
      meta: {
        title: '员工管理',
        icon: 'staff'
      }
    },
    {
      path: 'drag-table',
      component: () => import('@/views/table/dragTable'),
      name: 'dragTable',
      meta: {
        title: '部门管理',
        icon: 'department'
      }
    } */
  ]
}
export default organizationRouter
