import { render, screen } from "@testing-library/react";
import ProfileUserCard from "../../components/cards/profileUserCard";
import { getUserMockedData } from "../../__mocks__/mockedData";

const user = getUserMockedData()

const mockUsedNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
   ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockUsedNavigate,
}));

describe('Test Profile User Card', ()=>{

    test('Test that card renders correctly for a given user', ()=>{
        render(<ProfileUserCard user={user} inProfileView={false}/>)
        expect(screen.getByText(user.name)).toBeInTheDocument()
    })

    test('Test that card renders correctly with boolean', ()=>{
        render(<ProfileUserCard user={user} inProfileView={true}/>)
        expect(screen.getByText(user.name)).toBeInTheDocument()
        expect(screen.getByText("Contact")).toBeInTheDocument()
    })

})