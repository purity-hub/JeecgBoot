import { FormSchema } from '@/components/Table';

export const formSchemas: FormSchema[] = [
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
        onChange: (e: any) => {},
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
