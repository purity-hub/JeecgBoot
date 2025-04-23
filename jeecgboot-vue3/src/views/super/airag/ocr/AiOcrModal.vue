<script setup lang="ts">
  import { BasicForm, FormSchema, useForm } from '@/components/Form';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { saveOrUpdateOcr } from '@/views/super/airag/ocr/ocr.api';
  import { ref, unref } from 'vue';

  // 引入
  import { createWorker } from 'tesseract.js';
  import { useGlobSetting } from '@/hooks/setting';

  const language = ref<string>('chi_sim');

  const formSchemas: FormSchema[] = [
    {
      label: '',
      field: 'id',
      component: 'Input',
      show: false,
    },
    {
      label: '标题(文件名)',
      field: 'fileName',
      component: 'Input',
    },
    {
      label: '图片',
      field: 'imagePath',
      component: 'JImageUpload',
      componentProps: {
        text: '图片上传',
        fileMax: 1,
        //支持两种基本样式picture和picture-card
        listType: 'picture-card',
        bizPath: 'ocr',
        disabled: false,
        onChange: (e: string) => {
          imagePath.value = e;
        },
      },
    },
    {
      label: '语言',
      field: 'language',
      component: 'Select',
      componentProps: ({ formModel, formActionType }) => {
        return {
          options: [
            {
              label: '中文',
              value: 'chi_sim',
            },
            {
              label: '英文',
              value: 'eng',
            },
            {
              label: '日文',
              value: 'jpn',
            },
          ],
          placeholder: '请选择语言',
          onChange: (e: any) => {
            language.value = e;
          },
        };
      },
      show: true,
    },
    {
      label: '识别结果',
      field: 'ocrResult',
      component: 'InputTextArea',
    },
  ];

  // 声明 emits
  const emit = defineEmits(['success', 'register']);
  const title = ref<string>('');
  const isUpdate = ref<boolean>(false);
  const isParse = ref<boolean>(false);

  // 解析的图片路径
  const imagePath = ref<string>('');
  // 解析的结果
  const parseResult = ref<string>('');

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

    if (isUpdate.value) {
      formSchemas[3].show = false;
    }

    // 开启解析时，设置图片路径和结果
    if (unref(data.isParse)) {
      imagePath.value = unref(data.record.imagePath);
    }
    await resetFields();
    await setFieldsValue({ ...data.record });
    // 隐藏底部时禁用整个表单
    await setProps({ disabled: !data?.showFooter });
  });

  const globSetting = useGlobSetting();
  const baseApiUrl = globSetting.domainUrl;

  async function onSubmit() {
    try {
      if (isParse.value) {
        // 选择语言
        const worker = await createWorker(language.value);
        console.log(baseApiUrl + '/sys/common/static/' + imagePath.value);
        const ret = await worker.recognize(baseApiUrl + '/sys/common/static/' + imagePath.value);
        console.log(ret.data.text);
        await setFieldsValue({ ocrResult: ret.data.text });
        await worker.terminate();
        isParse.value = false;
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
  <BasicModal @register="registerModal" :title="title" :width="800" v-bind="$attrs" @ok="onSubmit" :okText="isParse ? '解析' : '提交'">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<style scoped lang="less"></style>
