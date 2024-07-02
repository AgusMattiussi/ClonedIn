import { useRequestApi } from "../api/apiRequest"

export const usePutUserContactStatus = () => {
  const { apiRequest } = useRequestApi()

  async function answerUserContact(
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

  return { answerUserContact }
}
