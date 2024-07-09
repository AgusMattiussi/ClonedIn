import { renderHook, act } from '@testing-library/react';
import { useGetCategories } from '../../hooks/useGetCategories';
import { useRequestApi } from '../../api/apiRequest'; 

// Mock useRequestApi
jest.mock('../../api/apiRequest', () => ({
    useRequestApi: jest.fn(),
}));
  

describe('useGetCategories', () => {
  it('fetches categories successfully', async () => {
    const mockApiRequest = jest.fn();
    (useRequestApi as jest.Mock).mockReturnValue({ apiRequest: mockApiRequest });

    const { result } = renderHook(() => useGetCategories());

    // Mock API request response
    const mockResponse = { status: 200, data: ['Technology', 'Economics'] };
    mockApiRequest.mockResolvedValue(mockResponse);

    let categories;
    await act(async () => {
      categories = await result.current.getCategories();
    });

    expect(mockApiRequest).toHaveBeenCalledWith({
      url: '/categories',
      method: 'GET',
    });
    expect(categories).toEqual(mockResponse);
  });
});