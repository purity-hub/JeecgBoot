<script setup lang="ts">
  import { BasicForm, useForm } from '@/components/Form';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { saveOrUpdate } from '@/views/system/message/template/template.api';
  import { formSchemas } from './ocr.data';
  import { ref, unref } from 'vue';

  // 声明 emits
  const emit = defineEmits(['success', 'register']);
  const title = ref<string>('');
  const isUpdate = ref<boolean>(false);
  // 注册 form
  //update-begin---author:wangshuai ---date:20221123  for：[VUEN-2807]消息模板加一个查看功能------------
  const [registerForm, { resetFields, setFieldsValue, validate, updateSchema, setProps }] = useForm({
    //update-end---author:wangshuai ---date:20221123  for：[VUEN-2807]消息模板加一个查看功能--------------z
    schemas: formSchemas,
    showActionButtonGroup: false,
  });
  // 注册 modal
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false, showCancelBtn: !!data?.showFooter, showOkBtn: !!data?.showFooter });
    isUpdate.value = unref(data.isUpdate);
    title.value = unref(data.title);
    await resetFields();
    await setFieldsValue({ ...data.record });
    // 隐藏底部时禁用整个表单
    setProps({ disabled: !data?.showFooter });
  });

  async function onSubmit() {
    try {
      const values = await validate();
      setModalProps({ confirmLoading: true });
      // 提交表单
      await saveOrUpdate(values, isUpdate);
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
