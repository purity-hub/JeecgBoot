<script setup lang="ts">
  import { BasicForm, useForm } from '@/components/Form';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { saveOrUpdateOcr } from '@/views/super/airag/ocr/ocr.api';
  import { formSchemas } from './ocr.data';
  import { ref, unref } from 'vue';

  // 声明 emits
  const emit = defineEmits(['success', 'register']);
  const title = ref<string>('');
  const isUpdate = ref<boolean>(false);
  const isParse = ref<boolean>(false);
  // 注册 form
  const [registerForm, { resetFields, setFieldsValue, validate, updateSchema, setProps }] = useForm({
    schemas: formSchemas,
    showActionButtonGroup: false,
  });
  // 注册 modal
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false, showCancelBtn: !!data?.showFooter, showOkBtn: !!data?.showFooter });
    isUpdate.value = unref(data.isUpdate);
    isParse.value = unref(data.isParse);
    title.value = unref(data.title);
    await resetFields();
    await setFieldsValue({ ...data.record });
    // 隐藏底部时禁用整个表单
    setProps({ disabled: !data?.showFooter });
  });

  async function onSubmit() {
    try {
      if (isParse) {
        return;
      }
      const values = await validate();
      setModalProps({ confirmLoading: true });
      // 提交表单
      await saveOrUpdateOcr(values, isUpdate);
      //关闭弹窗
      closeModal();
      //刷新列表
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>

<template>
  <BasicModal @register="registerModal" :title="title" :width="800" v-bind="$attrs" @ok="onSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<style scoped lang="less"></style>
