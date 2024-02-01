import { useRequestApi } from "../api/apiRequest"

export const usePutImage = () => {
  const { apiRequest } = useRequestApi()

  async function putImage(requestUrl: string, formData: FormData) {
    const response = await apiRequest({
      url: requestUrl,
      method: "PUT",
      body: formData,
      headers: {
        "Content-Type": "multipart/form-data",
      },
    })
    return response
  }

  return { putImage }
}
