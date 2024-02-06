import "./UserManagement.css";
import {Pagination, Table, TableBody, TableCell, TableHead, TableRow} from "@mui/material";
import {useEffect, useState} from "react";
import {getUsersApi} from "../../api/userApi";

export default function UserManagement() {
    const [users, setUsers] = useState([]);
    const [totalCount, setTotalCount] = useState(0);
    const [pages, setPages] = useState(0);
    const [pageNo, setPageNo] = useState(1);
    const pageSize = 10;

    function displayFullName(firstName, lastName) {
        return (firstName ? firstName : "") +
            (lastName ? " " + lastName : "");
    }

    function getUsers() {
        const params = {}

        params.pageNo = pageNo
        params.pageSize = pageSize

        getUsersApi(params)
            .then(response => {
                setTotalCount(response.resultBody.totalCount);
                setPages(Math.floor((response.resultBody.totalCount + pageSize - 1) / pageSize));
                setUsers(response.resultBody.dataList);
            });
    }

    useEffect(() => {
        getUsers();
    }, [pageNo]);

    return <div>
        <Table>
            <TableHead className={"User-table-head"}>
                <TableRow>
                    <TableCell style={{fontSize: "1.8rem", borderTopLeftRadius: "1rem", borderRight: "1px solid #B2B2B2"}}
                               align={"center"}>Name</TableCell>
                    <TableCell style={{fontSize: "1.8rem", borderRight: "1px solid #B2B2B2"}}
                               align={"center"}>Phone No.</TableCell>
                    <TableCell style={{fontSize: "1.8rem", borderTopRightRadius: "1rem"}} align={"center"}>Actions</TableCell>
                </TableRow>
            </TableHead>
            <TableBody style={{borderLeft: "1px solid #D2D2D2", borderRight: "1px solid #D2D2D2"}}>
                {users.map((user, index) => {
                    return <TableRow key={"UserRow-" + index}>
                        <TableCell style={{fontSize: "1.5rem", borderRight: "1px solid #D2D2D2"}} align={"center"}>{displayFullName(user.firstName, user.lastName)}</TableCell>
                        <TableCell style={{fontSize: "1.5rem", borderRight: "1px solid #D2D2D2"}} align={"center"}>{user.phoneNo}</TableCell>
                        <TableCell style={{fontSize: "1.5rem"}} align={"center"}>
                            <div className={"User-roles-button"}>Roles</div>
                        </TableCell>
                    </TableRow>;
                })}
            </TableBody>
        </Table>
        <Pagination style={{float: "right", marginTop: "1rem", fontSize: "1.5rem"}} count={pages} page={pageNo}
                    onChange={(e, page) => {setPageNo(page)}}/>
        <span className={"User-total-count"}>{totalCount} in total</span>
    </div>
}