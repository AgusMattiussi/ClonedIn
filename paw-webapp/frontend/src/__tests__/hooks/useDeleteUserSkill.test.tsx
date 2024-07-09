import { renderHook, act } from '@testing-library/react';
import { useDeleteUserSkill } from '../../hooks/useDeleteUserSkill';
import { useRequestApi } from '../../api/apiRequest'; 

// Mock useRequestApi
jest.mock('../../api/apiRequest', () => ({
    useRequestApi: jest.fn(),
  }));
  
  describe('useDeleteUserSkill', () => {
    it('deletes user experience successfully', async () => {
      const mockApiRequest = jest.fn();
      (useRequestApi as jest.Mock).mockReturnValue({ apiRequest: mockApiRequest });
  
      const { result } = renderHook(() => useDeleteUserSkill());
  
      // Mock API request response
      const mockResponse = { status: 200, data: { message: 'Experience deleted successfully' } };
      mockApiRequest.mockResolvedValue(mockResponse);
  
      let deleteResult;
      await act(async () => {
        deleteResult = await result.current.deleteUserSkill('user_id_123', 456);
      });
  
      expect(mockApiRequest).toHaveBeenCalledWith({
        url: '/users/user_id_123/skills/456',
        method: 'DELETE',
      });
      expect(deleteResult).toEqual(mockResponse);
    });
  });
