<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="filters.name" placeholder="名称" style="width: 200px;" class="filter-item"/>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">{{ $t('table.search') }}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">{{ $t('table.add') }}</el-button>
    </div>

    <el-table v-loading="pageLoading" :key="tableKey" :data="pagination.content" border fit highlight-current-row style="width: 100%;min-height:1000px;" @sort-change="handleSort">
      <el-table-column :label="$t('table.id')" prop="id" align="center" width="100" sortable>
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column min-width="250px" prop="name" label="名称" sortable>
        <template slot-scope="scope">
          <span class="link-type" @click="handleUpdate(scope.row)">{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.actions')" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">{{ $t('table.edit') }}</el-button>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.date')" prop="createTime" width="190px" align="center" sortable>
        <template slot-scope="scope">
          <span>{{ scope.row.createTime | datetime }}</span>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination :current-page.sync="pagination.number + 1" :page-sizes="[10,20,30, 50]" :page-size="pagination.size" :total="pagination.totalElements" background layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange"/>
    </div>

    <el-dialog :title="action" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="formObj" label-position="left" label-width="70px" style="width: 400px; margin-left:50px;">
        <el-form-item label="名称" prop="title">
          <el-input v-model="formObj.name"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">{{ $t('table.cancel') }}</el-button>
        <el-button v-if="action==='create'" type="primary" @click="executeCreate">{{ $t('table.confirm') }}</el-button>
        <el-button v-else type="primary" @click="executeUpdate">{{ $t('table.confirm') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchPage, create, update } from '@/api/location/country'

import waves from '@/directive/waves/index' // 水波纹指令

export default {
  name: 'CountryProject',
  directives: {
    waves
  },
  data() {
    return {
      tableKey: 0,
      pageLoading: true,
      pageQuery: {
        page: 1,
        size: 20,
        sort: 'id,desc'
      },
      filters: {
        name: null,
        createTimeRange: [],
        updateTimeRange: []
      },
      pagination: {
        totalElements: 0,
        number: 1,
        content: []
      },
      formObj: this.initFormObj(),
      action: null,
      dialogFormVisible: false,
      rules: {
        name: [
          {required: true, message: '请输入名称', trigger: 'blur'}
        ]
      }
    }
  },
  created() {
    this.loadPagination()
  },
  methods: {
    initFormObj() {
      return {
        id: null,
        name: null,
        nameEn: null
      }
    },
    loadPagination() {
      this.pageLoading = true
      fetchPage(this.pageQuery).then(response => {
        this.pagination = response.data
        // Just to simulate the time of the request
        setTimeout(() => {
          this.pageLoading = false
        }, 1.5 * 1000)
      })
    },
    handleSort: function(sort) {
      if (!sort || !sort.column || !sort.column.sortable) {
        return
      }
      this.pageQuery.sort = sort.prop + (sort.order === 'descending' ? ',desc' : '')
      this.loadPagination()
    },
    handleFilter() {
      this.pageQuery.page = 1
      this.loadPagination()
    },
    handleSizeChange(val) {
      this.pageQuery.size = val
      this.loadPagination()
    },
    handleCurrentChange(val) {
      this.pageQuery.page = val
      this.loadPagination()
    },
    resetTemp() {
      this.formObj = this.initFormObj()
    },
    handleCreate() {
      this.resetTemp()
      this.action = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    executeCreate() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          create(this.formObj).then(() => {
            this.pagination.content.unshift(this.formObj)
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleUpdate(row) {
      this.formObj = Object.assign({}, row) // copy obj
      this.action = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    executeUpdate() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.formObj)
          update(tempData).then(() => {
            for (const v of this.pagination.content) {
              if (v.id === this.formObj.id) {
                const index = this.pagination.content.indexOf(v)
                this.pagination.content.splice(index, 1, this.formObj)
                break
              }
            }
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleDelete(row) {
      this.$notify({
        title: '成功',
        message: '删除成功',
        type: 'success',
        duration: 2000
      })
      const index = this.pagination.content.indexOf(row)
      this.pagination.content.splice(index, 1)
    }
  }
}
</script>
