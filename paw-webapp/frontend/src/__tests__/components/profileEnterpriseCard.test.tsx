import { render, screen } from "@testing-library/react";
import ProfileEnterpriseCard from "../../components/cards/profileEnterpriseCard";
import { getEnterpriseMockedData } from "../../__mocks__/mockedData";

const enterprise = getEnterpriseMockedData()

const mockUsedNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
   ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockUsedNavigate,
}));

describe('Test Profile Enterprise Card', ()=>{

    test('Test that card renders correctly for a given enterprise', ()=>{
        render(<ProfileEnterpriseCard enterprise={enterprise}/>)
        expect(screen.getByText(enterprise.name)).toBeInTheDocument()
    })

})