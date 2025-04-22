import { defHttp } from '@/utils/http/axios';
import { Modal } from 'ant-design-vue';

enum Api {
  deleteOcr = '/airag/ocr/delete',
  deleteBatch = '/airag/ocr/deleteBatch',
  edit = '/airag/ocr/edit',
  save = '/airag/ocr/edit',
  list = '/airag/ocr/list',
}
export const deleteOcr = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.deleteOcr, params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
};

export const batchDeleteOcr = (params, handleSuccess) => {
  Modal.confirm({
    title: '确认删除',
    content: '是否删除选中数据',
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      return defHttp.delete({ url: Api.deleteBatch, data: params }, { joinParamsToUrl: true }).then(() => {
        handleSuccess();
      });
    },
  });
};

export const saveOrUpdateOcr = (params, isUpdate) => {
  let url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({ url: url, params });
};

export const getOcrList = (params) => {
  return defHttp.get({ url: Api.list, params });
};
