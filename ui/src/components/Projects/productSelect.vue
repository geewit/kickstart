<template>
  <el-select
    v-model="currentValue"
    :has-all="hasAll"
    :placeholder="placeholderText"
    :key-name="keyName"
    :label-name="labelName"
    :disable-name="disableName"
    :with-disabled="withDisabled"
    :disabled="disabled"
    :clearable="clearable"
    :multiple="multiple"
    :filterable="filterable"
    :filter-size="filterSize"
    :remote="remotable"
    :remote-method="remoteMethod">
    <el-option v-if="hasAll" label="全部" value=""/>
    <el-option v-for="option in options" :loading="isLoading" :key="option[keyName]" :label="option[labelName]" :value="option[keyName]" :disabled="option[disableName]"/>
  </el-select>
</template>
<script>
import component from '../component'

export default {
  name: 'ProductSelect',
  model: {
    prop: 'value',
    event: 'change'
  },
  props: {
    value: {
      type: Number,
      required: false,
      default: null
    },
    hasAll: {
      type: Boolean,
      required: false,
      default: false
    },
    placeholderText: {
      type: String,
      required: false,
      default: ''
    },
    keyName: {
      type: String,
      required: false,
      default: 'value'
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
    multiple: {
      type: Boolean,
      required: false,
      default: false
    },
    filterable: {
      type: Boolean,
      required: false,
      default: true
    },
    filterSize: {
      type: Number,
      required: false,
      default: 0
    },
    remotable: {
      type: Boolean,
      required: false,
      default: false
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
        console.debug('get - this.value = ' + this.value + ', this.keyName = ' + this.keyName + ', this.labelName = ' + this.labelName)
        return this.value
      },
      set(val) {
        this.$emit('change', val, this.options)
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
      console.debug('remotable: ' + this.remotable)
      console.debug('input: ' + input)
      console.debug('this.value: ' + this.value)
      console.groupEnd()

      const url = 'api/business/product/component/true'
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
