import { useRequestApi } from "../api/apiRequest"

export const usePutEnterpriseContactStatus = () => {
  const { apiRequest } = useRequestApi()

  async function answerEnterpriseContact(
    contactId: string | undefined,
    status: string
  ) {
    const response = await apiRequest({
      url: `/contacts/${contactId}`,
      method: "POST",
      body: {
        status,
      },
    })
    return response
  }

  return { answerEnterpriseContact }
}
