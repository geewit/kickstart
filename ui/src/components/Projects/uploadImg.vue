<template>
  <div>
    <el-upload
      ref="uploadImg"
      :file-list="fileList"
      :action="uploadUrl"
      :data="data"
      :multiple="multiple"

      :limit="limitNum"
      :max-size="maxSize"
      :accept="acceptType"
      :before-upload="handleBeforeUpload"
      :on-success="handleSuccess"
      :on-error="handleError"

      :on-preview="handlePictureCardPreview"
      :on-remove="handleRemove"
      :on-exceed="handleLimitNum"
      class="upload-img"
      drag
      list-type="picture-card">

      <i class="el-icon-plus"/>
    </el-upload>

    <el-dialog :visible.sync="dialogVisible" :modal="false">
      <img :src="dialogImageUrl" width="100%">
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'UploadImg',
  props: {
    uploadUrl: {
      type: String,
      required: true,
      default: ''
    },
    data: {
      type: Object,
      required: false,
      default: null
    },
    multiple: {
      type: Boolean,
      required: false,
      default: true
    },
    limitNum: {
      type: Number,
      required: false,
      default: 99
    },
    // 判断值和提示值必须对应
    maxSize: {
      type: Number,
      required: false,
      default: 2 * 1024 * 1024 // 2M = 2*1024*1024
    },
    maxSizeTip: {
      type: String,
      required: false,
      default: '2M' // 2M = 2*1024*1024
    },
    acceptType: {
      type: String,
      required: false,
      default: 'image/*'
    }
  },
  data() {
    return {
      dialogImageUrl: '',
      dialogVisible: false,
      fileList: []
    }
  },
  methods: {
    // 上传之前,判断图片数量和大小（return false 终止上传）
    handleBeforeUpload(file) {
      console.log(file)
      console.log(this.fileList)

      // 图片大小
      if (file.size > this.maxSize) {
        this.$message.error('图片文件太大，应小于MAXSIZE'.replace('MAXSIZE', this.maxSizeTip))
        return false
      }

      // 图片类型
      if (file.type.split('/')[0] !== 'image') {
        this.$message.error('仅支持上传图片')
        return false
      }

      // 文件查重
      for (let i = 0; i < this.fileList.length; i++) {
        const fileItem = this.fileList[i]
        if (file.name === fileItem.name && file.type === fileItem.raw.type && file.size === fileItem.size) {
          this.$message.error('图片重复，请选择其他图片上传')
          return false
        }
      }
    },
    // 超出最大数量限制
    handleLimitNum() {
      this.$message.error('最多上传LIMITNUM张图片'.replace('LIMITNUM', this.limitNum))
    },

    // 上传成功
    handleSuccess(response, file, fileList) {
      // console.log(response);
      // console.log(file);
      // 更新图片列表
      this.fileList = fileList
      this.$emit('uploadSuccess', response, fileList)
    },
    // 上传失败
    handleError(err) {
      console.log(err)
      this.$message.error('图片上传失败，请重试')
      this.$emit('uploadError', err)
    },
    // 删除图片
    handleRemove(file, fileList) {
      // 更新图片列表
      this.fileList = fileList
      this.$emit('removeImg', fileList)
    },
    // 预览图片
    handlePictureCardPreview(file) {
      console.log('handlePictureCardPreview')
      console.log(file)
      // 使用本地文件路径预览
      this.dialogImageUrl = file.url
      this.dialogVisible = true
    },

    // 清理文件列表
    clearFileList() {
      // console.log("clearImgList");
      // this.$refs.uploadImg.clearFiles();
      this.fileList = []
    }
  }
}
</script>
<style>
    .upload-img .el-upload-dragger {
        width: 148px;
        height: 148px;
    }
</style>
