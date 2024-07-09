import { renderHook, act } from '@testing-library/react';
import { useDeleteUserExperience } from '../../hooks/useDeleteUserExperience';
import { useRequestApi } from '../../api/apiRequest'; 

// Mock useRequestApi
jest.mock('../../api/apiRequest', () => ({
    useRequestApi: jest.fn(),
  }));
  
  describe('useDeleteUserExperience', () => {
    it('deletes user experience successfully', async () => {
      const mockApiRequest = jest.fn();
      (useRequestApi as jest.Mock).mockReturnValue({ apiRequest: mockApiRequest });
  
      const { result } = renderHook(() => useDeleteUserExperience());
  
      // Mock API request response
      const mockResponse = { status: 200, data: { message: 'Experience deleted successfully' } };
      mockApiRequest.mockResolvedValue(mockResponse);
  
      let deleteResult;
      await act(async () => {
        deleteResult = await result.current.deleteUserExperience('user_id_123', 456);
      });
  
      expect(mockApiRequest).toHaveBeenCalledWith({
        url: '/users/user_id_123/experiences/456',
        method: 'DELETE',
      });
      expect(deleteResult).toEqual(mockResponse);
    });
  });
