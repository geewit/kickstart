<template>
  <el-select
    v-model="currentValue"
    :type="typeValue"
    :key-name="keyName"
    :label-name="labelName"
    :disable-name="disableName"
    :with-disabled="withDisabled"
    :disabled="disabled"
    :clearable="clearable"
    :filterable="filterable"
    :multiple="multiple"
    :placeholder="placeholderText"
    :remote="remotable"
    :filter-size="filterSize"
    :remote-method="remoteMethod">
    <el-option v-for="option in options" :loading="isLoading" :key="option[keyName]" :label="option[labelName]" :value="option[keyName]" :disabled="option[disableName]"/>
  </el-select>
</template>
<script>
import component from 'index'
import { isEmpty } from '@/utils'

export default {
  name: 'Locations',
  model: {
    prop: 'value',
    event: 'change'
  },
  props: {
    value: {
      type: String,
      required: false,
      default: null
    },
    type: {
      type: String,
      required: true,
      default: null
    },
    keyName: {
      type: String,
      required: false,
      default: 'id'
    },
    labelName: {
      type: String,
      required: false,
      default: 'name'
    },
    disableName: {
      type: String,
      required: false,
      default: 'disabled'
    },
    withDisabled: {
      type: Boolean,
      required: false,
      default: true
    },
    disabled: {
      type: Boolean,
      required: false,
      default: false
    },
    clearable: {
      type: Boolean,
      required: false,
      default: true
    },
    filterable: {
      type: Boolean,
      required: false,
      default: true
    },
    multiple: {
      type: Boolean,
      required: false,
      default: false
    },
    remotable: {
      type: Boolean,
      required: false,
      default: false
    },
    filterSize: {
      type: Number,
      required: false,
      default: 0
    },
    placeholderText: {
      type: String,
      required: false,
      default: '请选择位置'
    }
  },
  data() {
    return {
      isLoading: false,
      options: null
    }
  },
  computed: {
    currentValue: {
      get() {
        console.debug('get - this.value = ' + this.value + ', this.keyName = ' + this.keyName)
        return this.value
      },
      set(val) {
        this.$emit('change', val, this.options)
      }
    },
    typeValue: {
      get() {
        console.group('-----type-------')
        console.debug('value: ' + this.value)
        console.debug('type: ' + this.type)
        console.groupEnd()
        if (isEmpty(this.type)) {
          console.log('type is null')
          this.options = []
          return null
        } else {
          this.fetchOptions(null)
          return this.type
        }
      }
    }
  },
  mounted() {
    if (this.remotable === false || this.filterSize === 0) {
      this.fetchOptions(null)
    }
  },
  methods: {
    fetchOptions(input) {
      console.group('-----locations.fetchOptions-------')
      console.debug('type: ' + this.type)
      console.debug('remotable: ' + this.remotable)
      console.debug('input: ' + input)
      console.debug('this.value: ' + this.value)
      console.groupEnd()
      if (isEmpty(this.type)) {
        return
      }
      const url = process.env.API_URL_PREFIX + 'api/location/' + this.type + '/list'
      component.fetchOptions(url, input, this)
    },
    remoteMethod(input) {
      console.group('-----locations.remoteMethod-------')
      console.debug('input: ' + input)
      console.groupEnd()
      component.remoteMethod(input, this)
    }
  }
}
</script>
<style scoped="">
</style>
