import { renderHook, act } from '@testing-library/react';
import { usePostContact } from '../../hooks/usePostContact';
import { useRequestApi } from '../../api/apiRequest'; 

// Mock useRequestApi
jest.mock('../../api/apiRequest', () => ({
    useRequestApi: jest.fn(),
}));
  

describe('usePostContact', () => {
  it('creates contact successfully', async () => {
    const mockApiRequest = jest.fn();
    (useRequestApi as jest.Mock).mockReturnValue({ apiRequest: mockApiRequest });

    const { result } = renderHook(() => usePostContact());

    // Mock API request response
    const mockResponse = { status: 201, data: { message: 'Contact created successfully' } };
    mockApiRequest.mockResolvedValue(mockResponse);

    const message = "Hello World"
    const jobOfferId = "22"
    const userId = "28"

    let postResult;
    await act(async () => {
      postResult = await result.current.addContact(message, jobOfferId, userId);
    });

    expect(mockApiRequest).toHaveBeenCalledWith({
        url: `/contacts`,
        method: "POST",
        body: {
          message,
          jobOfferId,
          userId,
        },
    });
    expect(postResult).toEqual(mockResponse);
  });
});