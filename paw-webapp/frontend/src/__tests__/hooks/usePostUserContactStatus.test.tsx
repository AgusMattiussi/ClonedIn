import { renderHook, act } from '@testing-library/react';
import { usePostUserContactStatus } from '../../hooks/usePostUserContactStatus';
import { useRequestApi } from '../../api/apiRequest'; 

// Mock useRequestApi
jest.mock('../../api/apiRequest', () => ({
    useRequestApi: jest.fn(),
}));
  

describe('usePostUserContactStatus', () => {
  it('updates user contact status successfully', async () => {
    const mockApiRequest = jest.fn();
    (useRequestApi as jest.Mock).mockReturnValue({ apiRequest: mockApiRequest });

    const { result } = renderHook(() => usePostUserContactStatus());

    // Mock API request response
    const mockResponse = { status: 200, data: { message: 'User contact status successfully'} };
    mockApiRequest.mockResolvedValue(mockResponse);

    const id = "28"
    const status = "Accepted"
   
    let postResult;
    await act(async () => {
      postResult = await result.current.answerUserContact(id, status);
    });

    expect(mockApiRequest).toHaveBeenCalledWith({
        url: `/contacts/${id}`,
        method: "POST",
        body: {
          status,
        },
    });
    expect(postResult).toEqual(mockResponse);
  });
});