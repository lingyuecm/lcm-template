import {getAccessToken} from "../../utils/cacheManager";
import styled from "styled-components";

const Paragraph = styled.p`
    font-size: 1.5rem;
    text-indent: 2ch;
`

export default function Dashboard() {
    return <div>
        <Paragraph>I don't know what should be displayed in this page for such a simple RBAC system,
            but I feel there should be a dashboard,
            so I'd like to display the access token below</Paragraph>
        <Paragraph>{getAccessToken()}</Paragraph>
    </div>
}
