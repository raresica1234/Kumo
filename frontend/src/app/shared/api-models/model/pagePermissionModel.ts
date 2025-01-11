/**
 * OpenAPI definition
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: v0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { PageableObject } from './pageableObject';
import { PermissionModel } from './permissionModel';
import { SortObject } from './sortObject';


export interface PagePermissionModel { 
    totalElements?: number;
    totalPages?: number;
    pageable?: PageableObject;
    first?: boolean;
    last?: boolean;
    size?: number;
    content?: Array<PermissionModel>;
    number?: number;
    sort?: SortObject;
    numberOfElements?: number;
    empty?: boolean;
}

